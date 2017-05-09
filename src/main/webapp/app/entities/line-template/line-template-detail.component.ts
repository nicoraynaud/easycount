import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { LineTemplate } from './line-template.model';
import { LineTemplateService } from './line-template.service';

@Component({
    selector: 'jhi-line-template-detail',
    templateUrl: './line-template-detail.component.html'
})
export class LineTemplateDetailComponent implements OnInit, OnDestroy {

    lineTemplate: LineTemplate;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private lineTemplateService: LineTemplateService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['lineTemplate', 'templateFrequency']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.lineTemplateService.find(id).subscribe(lineTemplate => {
            this.lineTemplate = lineTemplate;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
