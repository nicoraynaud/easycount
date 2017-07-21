import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Line } from './line.model';
import { LineService } from './line.service';

@Injectable()
export class LinePopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private lineService: LineService

    ) {}

    open(component: Component, id?: number | any, bankAccountId?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.lineService.find(id).subscribe((line) => {
                if (line.date) {
                    line.date = {
                        year: line.date.getFullYear(),
                        month: line.date.getMonth() + 1,
                        day: line.date.getDate()
                    };
                }
                line.createDate = this.datePipe
                    .transform(line.createDate, 'yyyy-MM-ddThh:mm');
                this.lineModalRef(component, line);
            });
        } else {
            return this.lineModalRef(component, this.lineService.createNew(bankAccountId, false));
        }
    }

    lineModalRef(component: Component, line: Line): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.line = line;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
