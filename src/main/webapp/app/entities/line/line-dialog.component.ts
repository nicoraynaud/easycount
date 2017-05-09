import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Line } from './line.model';
import { LinePopupService } from './line-popup.service';
import { LineService } from './line.service';
import { Category, CategoryService } from '../category';
import { BankAccount, BankAccountService } from '../bank-account';

import { IMultiSelectOption, IMultiSelectSettings, IMultiSelectTexts } from 'angular-2-dropdown-multiselect';
import {activateRoute} from '../../account/activate/activate.route';

@Component({
    selector: 'jhi-line-dialog',
    templateUrl: './line-dialog.component.html'
})
export class LineDialogComponent implements OnInit {

    line: Line;
    authorities: any[];
    isSaving: boolean;
    isSaveAndCreate = false;

    categoriesOptions: IMultiSelectOption[];
    categoriesSettings: IMultiSelectSettings = {
        pullRight: false,
        enableSearch: true,
        checkedStyle: 'fontawesome',
        buttonClasses: 'btn btn-default',
        selectionLimit: 0,
        closeOnSelect: false,
        showCheckAll: false,
        showUncheckAll: false,
        dynamicTitleMaxItems: 5,
        maxHeight: '300px',
    };
    categoriesTexts: IMultiSelectTexts = {
        checkAll: 'Check all',
        uncheckAll: 'Uncheck all',
        checked: 'checked',
        checkedPlural: 'checked',
        searchPlaceholder: 'Search...',
        defaultTitle: 'Select',
    };
    selectedCategories: number[];

    bankaccounts: BankAccount[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private lineService: LineService,
        private categoryService: CategoryService,
        private bankAccountService: BankAccountService,
        private eventManager: EventManager,
        private router: Router,
        private activatedRoute: ActivatedRoute
    ) {
        // Use addLocation i/o setLocations in order not to remove other necessary ones in the background
        this.jhiLanguageService.addLocation('line');
        this.jhiLanguageService.addLocation('lineStatus');
        this.jhiLanguageService.addLocation('lineSource');
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        let reqCategories = {
            page: 0,
            size: 500,
            sort: 'label,asc'
        };
        this.categoryService.query(reqCategories).subscribe(
            (res: Response) => {
                this.categoriesOptions = [];
                res.json().forEach(c =>
                    this.categoriesOptions.push( {id:  c.id, name: c.label} )
                );
                this.selectedCategories = new Array<number>();
                if (this.line.categories) {
                    this.line.categories.forEach(c => this.selectedCategories.push(c.id));
                }
            },
            (res: Response) => this.onError(res.json()));
        this.bankAccountService.query().subscribe(
            (res: Response) => { this.bankaccounts = res.json(); }, (res: Response) => this.onError(res.json()));
    }

    onChange($event) {
        // reset categories
        this.line.categories = new Array<Category>();
        // push again the categories
        this.selectedCategories.forEach(catId =>
            this.line.categories.push({ id: catId }));
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.line.id !== undefined) {
            this.lineService.update(this.line)
                .subscribe((res: Line) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.lineService.create(this.line)
                .subscribe((res: Line) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    saveAndCreate() {
        this.isSaveAndCreate = true;
        this.save();
    }

    private onSaveSuccess (result: Line) {
        this.eventManager.broadcast({ name: 'lineListModification', content: 'OK'});
        this.isSaving = false;
        if (this.isSaveAndCreate) {
            this.line = new Line().bankAccountId = this.line.bankAccountId;
        } else {
            this.activeModal.dismiss(result);
        }
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
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
    selector: 'jhi-line-popup',
    template: ''
})
export class LinePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private linePopupService: LinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.linePopupService
                    .open(LineDialogComponent, params['id']);
            } else if ( params['bankAccountId'] ) {
                this.modalRef = this.linePopupService
                    .open(LineDialogComponent, null, params['bankAccountId']);
            } else {
                this.modalRef = this.linePopupService
                    .open(LineDialogComponent);
            }

        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
