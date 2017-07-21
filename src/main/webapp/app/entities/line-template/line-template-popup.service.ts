import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LineTemplate } from './line-template.model';
import { LineTemplateService } from './line-template.service';

@Injectable()
export class LineTemplatePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private lineTemplateService: LineTemplateService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.lineTemplateService.find(id).subscribe((lineTemplate) => {
                if (lineTemplate.startDate) {
                    lineTemplate.startDate = {
                        year: lineTemplate.startDate.getFullYear(),
                        month: lineTemplate.startDate.getMonth() + 1,
                        day: lineTemplate.startDate.getDate()
                    };
                }
                this.lineTemplateModalRef(component, lineTemplate);
            });
        } else {
            return this.lineTemplateModalRef(component, new LineTemplate());
        }
    }

    lineTemplateModalRef(component: Component, lineTemplate: LineTemplate): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.lineTemplate = lineTemplate;
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
