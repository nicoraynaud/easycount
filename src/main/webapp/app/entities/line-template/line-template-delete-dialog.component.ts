import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { LineTemplate } from './line-template.model';
import { LineTemplatePopupService } from './line-template-popup.service';
import { LineTemplateService } from './line-template.service';

@Component({
    selector: 'jhi-line-template-delete-dialog',
    templateUrl: './line-template-delete-dialog.component.html'
})
export class LineTemplateDeleteDialogComponent {

    lineTemplate: LineTemplate;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private lineTemplateService: LineTemplateService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['lineTemplate']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.lineTemplateService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'lineTemplateListModification',
                content: 'Deleted an lineTemplate'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-line-template-delete-popup',
    template: ''
})
export class LineTemplateDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lineTemplatePopupService: LineTemplatePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.lineTemplatePopupService
                .open(LineTemplateDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
