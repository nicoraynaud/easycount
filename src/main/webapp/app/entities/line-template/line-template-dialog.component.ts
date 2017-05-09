import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { LineTemplate } from './line-template.model';
import { LineTemplatePopupService } from './line-template-popup.service';
import { LineTemplateService } from './line-template.service';
import { Line, LineService } from '../line';
import { Category, CategoryService } from '../category';
import { BankAccount, BankAccountService } from '../bank-account';

@Component({
    selector: 'jhi-line-template-dialog',
    templateUrl: './line-template-dialog.component.html'
})
export class LineTemplateDialogComponent implements OnInit {

    lineTemplate: LineTemplate;
    authorities: any[];
    isSaving: boolean;

    lines: Line[];

    categories: Category[];

    bankaccounts: BankAccount[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private lineTemplateService: LineTemplateService,
        private lineService: LineService,
        private categoryService: CategoryService,
        private bankAccountService: BankAccountService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['lineTemplate', 'templateFrequency']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.lineService.query().subscribe(
            (res: Response) => { this.lines = res.json(); }, (res: Response) => this.onError(res.json()));
        this.categoryService.query().subscribe(
            (res: Response) => { this.categories = res.json(); }, (res: Response) => this.onError(res.json()));
        this.bankAccountService.query().subscribe(
            (res: Response) => { this.bankaccounts = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.lineTemplate.id !== undefined) {
            this.lineTemplateService.update(this.lineTemplate)
                .subscribe((res: LineTemplate) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.lineTemplateService.create(this.lineTemplate)
                .subscribe((res: LineTemplate) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    private onSaveSuccess (result: LineTemplate) {
        this.eventManager.broadcast({ name: 'lineTemplateListModification', content: 'OK'});
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

    trackCategoryById(index: number, item: Category) {
        return item.id;
    }

    trackBankAccountById(index: number, item: BankAccount) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-line-template-popup',
    template: ''
})
export class LineTemplatePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private lineTemplatePopupService: LineTemplatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.lineTemplatePopupService
                    .open(LineTemplateDialogComponent, params['id']);
            } else {
                this.modalRef = this.lineTemplatePopupService
                    .open(LineTemplateDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
