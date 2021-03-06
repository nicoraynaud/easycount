import { LineService } from '../line/line.service';
import { Component, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import {AlertService, DateUtils, JhiLanguageService} from 'ng-jhipster';

import { BankAccount } from './bank-account.model';
import { BankAccountService } from './bank-account.service';
import { Currency } from '../currency/currency.model';
import { CurrencyService } from '../currency/currency.service';
import { ActivatedRoute } from '@angular/router';
import { Line } from '../line/line.model';

@Component({
    selector: 'jhi-bank-account-charts',
    templateUrl: './bank-account-charts.component.html',
    styleUrls: ['./bank-account-charts.component.scss']
})
export class BankAccountChartsComponent implements OnInit {

    bankAccountId: number;
    bankAccount: BankAccount;
    currency: Currency;
    subscription: any;

    searchQuery: string;
    currentSearch: string;
    showHelp: boolean;
    totalItems: any;
    advancedDebitPiePerCategory: any[];
    advancedCreditPiePerCategory: any[];
    monthlyData: any[];

    language: string;

    view: any[] = [450, 180];
    view2: any[] = [1100, 500];

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private bankAccountService: BankAccountService,
        private lineService: LineService,
        private route: ActivatedRoute,
        private currencyService: CurrencyService,
        private alertService: AlertService,
        private dateUtils: DateUtils
    ) {
        this.jhiLanguageService.setLocations(['bankAccount', 'bankAccountType', 'line', 'lineStatus', 'lineSource']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });

        this.jhiLanguageService.getCurrent().then((x) => this.language = x);
    }

    load(id) {
        this.bankAccountId = id;
        this.bankAccountService.find(this.bankAccountId).subscribe((bankAccount) => {
            this.currencyService.find(bankAccount.currencyId).subscribe((currency) => {
                this.currency = currency;
                this.bankAccount = bankAccount;
            });
        });
    }

    clear() {
        this.currentSearch = null;
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.searchQuery = `bankAccount.id:${this.bankAccountId} AND ${query}`;

        this.lineService.search({
            query: this.searchQuery,
            sort: ['date,asc'],
            page: 0,
            size: 100000
        }).subscribe(
            (res: Response) => this.onLoadLineSuccess(res.json(), res.headers),
            (res: Response) => this.onError(res.json())
        );
        return;
    }

    private onLoadLineSuccess(data, headers) {
        this.totalItems = headers.get('X-Total-Count');

        const sortedDebitData: Map<string, number> = new Map<string, number>();
        const sortedCreditData: Map<string, number> = new Map<string, number>();
        const sortedMonthlyDebitData: Map<string, number> = new Map<string, number>();
        const sortedMonthlyCreditData: Map<string, number> = new Map<string, number>();
        data.forEach((line: Line) => {
            const categoryLabel = line.categories.length !== 0 ? line.categories[0].label : 'none';
            const monthLabel = new Date(line.date).toLocaleDateString(this.language, { month: 'long', year: 'numeric' });
            if (line.debit && line.debit !== 0) {
                const previousValue = sortedDebitData.has(categoryLabel) ? sortedDebitData.get(categoryLabel) : 0;
                sortedDebitData.set(categoryLabel, Math.abs(previousValue + line.debit));

                const previousMonthValue = sortedMonthlyDebitData.has(monthLabel) ? sortedMonthlyDebitData.get(monthLabel) : 0;
                sortedMonthlyDebitData.set(monthLabel, previousMonthValue + line.debit);
            } else {
                const previousValue = sortedCreditData.has(categoryLabel) ? sortedCreditData.get(categoryLabel) : 0;
                sortedCreditData.set(categoryLabel, Math.abs(previousValue + line.credit));

                const previousMonthValue = sortedMonthlyCreditData.has(monthLabel) ? sortedMonthlyCreditData.get(monthLabel) : 0;
                sortedMonthlyCreditData.set(monthLabel, previousMonthValue + line.credit);
            }
        });

        this.advancedDebitPiePerCategory = [];
        sortedDebitData.forEach((value, key) =>
            this.advancedDebitPiePerCategory.push( {name:  key, value} )
        );

        this.advancedCreditPiePerCategory = [];
        sortedCreditData.forEach((value, key) =>
            this.advancedCreditPiePerCategory.push( {name:  key, value} )
        );

        this.monthlyData = [];
        sortedMonthlyDebitData.forEach((value, key) =>
            this.monthlyData.push( {name: key, series: [ {name: 'Debit', value} ] } )
        );
        sortedMonthlyCreditData.forEach((value, key) => {
            const month = this.monthlyData.find((i) => i.name === key);
            if (month) {
                month.series.push({name: 'Credit', value});
            } else {
                this.monthlyData.push( {name: key, series: [ {name: 'Credit', value} ] } );
            }
        });

    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    showHelpToggle() {
        this.showHelp = !this.showHelp;
    }
}
