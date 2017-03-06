import { Line } from '../line/line.model'
import { LineService } from '../line/line.service'
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';
import { BankAccount } from './bank-account.model';
import { BankAccountService } from './bank-account.service';

import { ITEMS_PER_PAGE, Principal } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';
import { Response } from '@angular/http'

@Component({
    selector: 'jhi-bank-account-dashboard',
    templateUrl: './bank-account-dashboard.component.html'
})
export class BankAccountDashboardComponent implements OnInit, OnDestroy {

    bankAccountId: number;
    bankAccount: BankAccount;
    lines: Line[];
    private subscription: any;

    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private bankAccountService: BankAccountService,
        private parseLinks: ParseLinks,
        private lineService: LineService,
        private route: ActivatedRoute,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private paginationUtil: PaginationUtil,
        private paginationConfig: PaginationConfig,
        private alertService: AlertService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 1;
        this.previousPage = this.page;
        this.reverse = 'asc';
        this.predicate = 'id';
        this.jhiLanguageService.setLocations(['bankAccount', 'bankAccountType', 'line', 'lineStatus', 'lineSource']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
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
    }

    trackId (index: number, item: Line) {
        return item.id;
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    sort () {
        let result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
}
