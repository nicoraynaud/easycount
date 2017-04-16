import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { LineTemplate } from './line-template.model';
import { LineTemplateService } from './line-template.service';

@Component({
    selector: 'jhi-line-template-detail',
    templateUrl: './line-template-detail.component.html'
})
export class LineTemplateDetailComponent implements OnInit, OnDestroy {

    lineTemplate: LineTemplate;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private lineTemplateService: LineTemplateService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['lineTemplate']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLineTemplates();
    }

    load(id) {
        this.lineTemplateService.find(id).subscribe((lineTemplate) => {
            this.lineTemplate = lineTemplate;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLineTemplates() {
        this.eventSubscriber = this.eventManager.subscribe('lineTemplateListModification', (response) => this.load(this.lineTemplate.id));
    }
}
