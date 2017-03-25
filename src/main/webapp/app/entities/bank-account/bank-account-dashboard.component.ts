import { Line } from '../line/line.model';
import { LineService } from '../line/line.service';
import {Component, OnInit, OnDestroy, HostListener} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';
import { BankAccount } from './bank-account.model';
import { BankAccountService } from './bank-account.service';

import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Response } from '@angular/http';
import { Subscription } from 'rxjs';

@Component({
    selector: 'jhi-bank-account-dashboard',
    templateUrl: './bank-account-dashboard.component.html',
    styleUrls: ['./bank-account-dashboard.component.css']
})
export class BankAccountDashboardComponent implements OnInit, OnDestroy {

    bankAccountId: number;
    bankAccount: BankAccount;
    lines: Line[];
    private subscription: any;
    eventSubscriber: Subscription;

    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    selectedLine : Line;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private bankAccountService: BankAccountService,
        private parseLinks: ParseLinks,
        private lineService: LineService,
        private route: ActivatedRoute,
        private router: Router,
        private eventManager: EventManager,
        private paginationUtil: PaginationUtil,
        private paginationConfig: PaginationConfig,
        private alertService: AlertService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 1;
        this.previousPage = this.page;
        this.reverse = false;
        this.predicate = 'date';
        this.jhiLanguageService.setLocations(['bankAccount', 'bankAccountType', 'line', 'lineStatus', 'lineSource']);
    }

    setClickedRow (line) {
        this.selectedLine = line;
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInLines();
    }

    registerChangeInLines() {
        this.eventSubscriber = this.eventManager.subscribe('lineListModification', (response) => this.loadLines());
    }

    load (id) {
        this.bankAccountId = id;
        this.bankAccountService.find(this.bankAccountId).subscribe(bankAccount => {
            this.bankAccount = bankAccount;
        });
        this.loadLines();
    }

    loadLines() {
        this.lineService.findByBankAccount(this.bankAccountId, {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: Response) => this.onSuccess(res.json(), res.headers),
            (res: Response) => this.onError(res.json())
        );
    }

    loadPage (page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    @HostListener('window:keydown', ['$event'])
    onKey($event) {
        switch ($event.code) {
            case 'KeyC':
                this.createLine();
                break;
            case 'KeyE':
                this.editLine(this.selectedLine);
                break;
            case 'KeyV':
                this.validateLine(this.selectedLine);
                break;
            case 'ArrowUp':
                this.upLine(this.selectedLine);
                break;
            case 'ArrowDown':
                this.downLine(this.selectedLine);
                break;
            default:
                break;
        }
    }

    /** KEY SHORTCUTS FOR NAVIGATION **/
    createLine() {
        this.router.navigate([{ outlets: { popup: 'line-new/'+ this.bankAccountId }}], { replaceUrl: true });
    }

    editLine (line) {
        if (line) {
            this.router.navigate([{outlets: {popup: 'line/' + line.id + '/edit'}}], {replaceUrl: true});
        }
    }

    validateLine (line) {
        if (line) {
            this.lineService.validateLine(line)
                .subscribe((res: Line) => this.onSaveSuccess(res), (res: Response) => this.onError(res.json()));
        }
    }
    private onSaveSuccess (result: Line) {
        this.eventManager.broadcast({ name: 'lineListModification', content: 'OK'});
    }

    upLine (line) {
        if (line && this.lines.indexOf(line) !== 0) {
            this.selectedLine = this.lines[this.lines.indexOf(line) - 1];
        } else {
            this.selectedLine = this.lines[this.lines.length - 1];
        }
    }

    downLine (line) {
        if (line && this.lines.indexOf(line) !== this.lines.length) {
            this.selectedLine = this.lines[this.lines.indexOf(line) + 1];
        } else {
            this.selectedLine = this.lines[0];
        }
    }

    /** NAVIGATION OF LINES **/

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    transition() {
        this.loadLines();
    }

    private onSuccess (data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.lines = data;

        // Set back the selected line after reload
        if (this.selectedLine) {
            this.selectedLine = this.lines.filter(x => x.id === this.selectedLine.id)[0];
        }
    }

    trackId (index: number, item: Line) {
        return item.id;
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    sort () {
        let result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        return result;
    }
}
