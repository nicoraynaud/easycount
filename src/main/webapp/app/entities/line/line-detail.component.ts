import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Line } from './line.model';
import { LineService } from './line.service';

@Component({
    selector: 'jhi-line-detail',
    templateUrl: './line-detail.component.html'
})
export class LineDetailComponent implements OnInit, OnDestroy {

    line: Line;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private lineService: LineService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['line', 'lineStatus', 'lineSource']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.lineService.find(id).subscribe(line => {
            this.line = line;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
