import { Line } from '../line/line.model';
import { LineService } from '../line/line.service';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';
import { BankAccount } from './bank-account.model';
import { BankAccountService } from './bank-account.service';

import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Response } from '@angular/http';
import { Subscription } from 'rxjs';
import { Currency } from '../currency/currency.model';
import { CurrencyService } from '../currency/currency.service';

@Component({
    selector: 'jhi-bank-account-dashboard',
    templateUrl: './bank-account-dashboard.component.html',
    styleUrls: ['./bank-account-dashboard.component.css']
})
export class BankAccountDashboardComponent implements OnInit, OnDestroy {

    bankAccountId: number;
    bankAccount: BankAccount;
    currency: Currency;
    lines: Line[];
    private subscription: any;
    eventSubscriber: Subscription;

    // routeData: any;
    currentSearch: string;
    searchQuery: string;
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
        private currencyService: CurrencyService,
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
        console.log("changed");
        this.eventSubscriber = this.eventManager.subscribe('lineListModification', (response) => this.load(this.bankAccountId));
    }

    load (id) {
        this.bankAccountId = id;
        this.bankAccountService.find(this.bankAccountId).subscribe(bankAccount => {
            this.currencyService.find(bankAccount.currencyId).subscribe(currency => {
                this.currency = currency;
                this.bankAccount = bankAccount;
                this.loadLines();
            });
        });
    }

    loadLines() {
        if (this.searchQuery && this.searchQuery !== '') {
            this.lineService.search({
                query: this.searchQuery,
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: Response) => this.onLoadLineSuccess(res.json(), res.headers),
                (res: Response) => this.onError(res.json())
            );
            return;
        }
        this.lineService.findByBankAccount(this.bankAccountId, {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: Response) => this.onLoadLineSuccess(res.json(), res.headers),
            (res: Response) => this.onError(res.json())
        );
    }

    loadPage (page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    clear () {
        this.page = 0;
        this.currentSearch = null;
        this.searchQuery = null;
        this.loadLines();
    }

    search (query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.searchQuery = `bankAccount.id:${this.bankAccountId} AND ${query}`;
        this.loadLines();
    }

    createLine() {
        this.router.navigate([{ outlets: { popup: 'line-new/'+ this.bankAccountId }}], { replaceUrl: true });
    }

    editLine (line) {
        if (line) {
            this.router.navigate([{outlets: {popup: 'line/' + line.id + '/edit'}}], {replaceUrl: true});
        }
    }

    tickLine (line) {
        if (line) {
            this.lineService.tickLine(line)
                .subscribe((res: Line) => this.onSaveSuccess(res), (res: Response) => this.onError(res.json()));
        }
    }

    cancelLine (line) {
        if (line) {
            this.lineService.cancelLine(line)
                .subscribe((res: Line) => this.onSaveSuccess(res), (res: Response) => this.onError(res.json()));
        }
    }

    private onSaveSuccess (result: Line) {
        this.eventManager.broadcast({ name: 'lineListModification', content: 'OK'});
    }

    /** STYLING / INTERFACE **/

    getColorOfStatusTicked (status: string, modulo: number) {
        if (status === 'TICKED') {
            return "green";
        } else {
            return modulo%2 == 0 ? "white" : "whitesmoke";
        }
    }

    getColorOfStatusCancelled (status: string, modulo: number) {
        if (status === 'CANCELLED') {
            return "red";
        } else {
            return modulo%2 == 0 ? "white" : "whitesmoke";
        }
    }

    /** KEY SHORTCUTS FOR NAVIGATION **/

    @HostListener('window:keydown', ['$event'])
    onKey($event) {
        switch ($event.code) {
            // case 'KeyC':
            //     this.createLine();
            //     break;
            // case 'KeyE':
            //     this.editLine(this.selectedLine);
            //     break;
            // case 'ArrowUp':
            //     this.upLine(this.selectedLine);
            //     break;
            // case 'ArrowDown':
            //     this.downLine(this.selectedLine) ;
            //     break;
            default:
                break;
        }
    }

    /** NAVIGATION OF LINES **/

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

    /** END NAVIGATION OF LINES **/

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    transition() {
        this.loadLines();
    }

    private onLoadLineSuccess (data, headers) {
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
        let result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc'), 'id,asc'];
        return result;
    }
}
