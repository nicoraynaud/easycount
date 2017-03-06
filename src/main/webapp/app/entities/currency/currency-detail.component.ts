import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Currency } from './currency.model';
import { CurrencyService } from './currency.service';

@Component({
    selector: 'jhi-currency-detail',
    templateUrl: './currency-detail.component.html'
})
export class CurrencyDetailComponent implements OnInit, OnDestroy {

    currency: Currency;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private currencyService: CurrencyService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['currency']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.currencyService.find(id).subscribe(currency => {
            this.currency = currency;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
