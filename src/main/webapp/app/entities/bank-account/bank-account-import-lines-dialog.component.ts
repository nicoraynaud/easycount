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
    selector: 'jhi-bank-account-import-lines-dialog',
    templateUrl: './bank-account-import-lines-dialog.component.html'
})
export class BankAccountImportLinesDialogComponent implements OnInit {

    bankAccount: BankAccount;
    file: any;

    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private bankAccountService: BankAccountService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];

    }

    onFileChange(event) {
        var files = event.srcElement.files;
        console.log(files);
        this.file = files[0];
    }

    import() {
        this.isSaving = true;
        this.bankAccountService.importLines(this.bankAccount.id, this.file)
            .subscribe((res: Response) => this.onSaveSuccess(), (res: Response) => this.onSaveError(res));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    private onSaveSuccess () {
        this.eventManager.broadcast({ name: 'lineListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss('ok');
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-bank-account-popup',
    template: ''
})
export class BankAccountImportLinesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private bankAccountPopupService: BankAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.bankAccountPopupService
                .open(BankAccountImportLinesDialogComponent, params['id']);

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
