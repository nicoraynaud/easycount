import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {EventManager, AlertService, JhiLanguageService} from 'ng-jhipster';

import { BankAccount } from './bank-account.model';
import { BankAccountPopupService } from './bank-account-popup.service';
import { BankAccountService } from './bank-account.service';

@Component({
    selector: 'jhi-bank-account-generate-lines-dialog',
    templateUrl: './bank-account-generate-lines-dialog.component.html'
})
export class BankAccountGenerateLinesDialogComponent implements OnInit {

    bankAccount: BankAccount;
    date: any;
    dateDp: any;

    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private bankAccountService: BankAccountService,
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService
    ) {
    }

    ngOnInit() {
        this.jhiLanguageService.addLocation('lineTemplate');
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    generate() {
        this.isSaving = true;
        this.bankAccountService.generateLines(this.bankAccount.id, this.date)
            .subscribe((res: Response) => this.onSaveSuccess(), (res: Response) => this.onSaveError(res));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    private onSaveSuccess() {
        this.eventManager.broadcast({ name: 'lineListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss('ok');
    }

    private onSaveError(error) {
        this.isSaving = false;
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-bank-account-popup',
    template: ''
})
export class BankAccountGenerateLinesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private bankAccountPopupService: BankAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.bankAccountPopupService
                .open(BankAccountGenerateLinesDialogComponent, params['id']);

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
