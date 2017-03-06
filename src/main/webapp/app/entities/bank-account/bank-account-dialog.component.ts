import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { BankAccount } from './bank-account.model';
import { BankAccountPopupService } from './bank-account-popup.service';
import { BankAccountService } from './bank-account.service';
import { Line, LineService } from '../line';
import { Bank, BankService } from '../bank';
import { Currency, CurrencyService } from '../currency';
@Component({
    selector: 'jhi-bank-account-dialog',
    templateUrl: './bank-account-dialog.component.html'
})
export class BankAccountDialogComponent implements OnInit {

    bankAccount: BankAccount;
    authorities: any[];
    isSaving: boolean;

    lines: Line[];

    banks: Bank[];

    currencies: Currency[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private bankAccountService: BankAccountService,
        private lineService: LineService,
        private bankService: BankService,
        private currencyService: CurrencyService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['bankAccount', 'bankAccountType']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.lineService.query().subscribe(
            (res: Response) => { this.lines = res.json(); }, (res: Response) => this.onError(res.json()));
        this.bankService.query().subscribe(
            (res: Response) => { this.banks = res.json(); }, (res: Response) => this.onError(res.json()));
        this.currencyService.query().subscribe(
            (res: Response) => { this.currencies = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.bankAccount.id !== undefined) {
            this.bankAccountService.update(this.bankAccount)
                .subscribe((res: BankAccount) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.bankAccountService.create(this.bankAccount)
                .subscribe((res: BankAccount) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: BankAccount) {
        this.eventManager.broadcast({ name: 'bankAccountListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackLineById(index: number, item: Line) {
        return item.id;
    }

    trackBankById(index: number, item: Bank) {
        return item.id;
    }

    trackCurrencyById(index: number, item: Currency) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-bank-account-popup',
    template: ''
})
export class BankAccountPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private bankAccountPopupService: BankAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.bankAccountPopupService
                    .open(BankAccountDialogComponent, params['id']);
            } else {
                this.modalRef = this.bankAccountPopupService
                    .open(BankAccountDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
