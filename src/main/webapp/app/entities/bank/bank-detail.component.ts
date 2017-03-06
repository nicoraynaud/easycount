import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { Bank } from './bank.model';
import { BankService } from './bank.service';

@Component({
    selector: 'jhi-bank-detail',
    templateUrl: './bank-detail.component.html'
})
export class BankDetailComponent implements OnInit, OnDestroy {

    bank: Bank;
    private subscription: any;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private bankService: BankService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['bank']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
    }

    load (id) {
        this.bankService.find(id).subscribe(bank => {
            this.bank = bank;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}
