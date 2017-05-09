import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Line } from './line.model';
import { LinePopupService } from './line-popup.service';
import { LineService } from './line.service';

@Component({
    selector: 'jhi-line-delete-dialog',
    templateUrl: './line-delete-dialog.component.html'
})
export class LineDeleteDialogComponent {

    line: Line;

    constructor(
        private lineService: LineService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.lineService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'lineListModification',
                content: 'Deleted an line'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-line-delete-popup',
    template: ''
})
export class LineDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private linePopupService: LinePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.linePopupService
                .open(LineDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
