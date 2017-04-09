import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { Subscription } from 'rxjs';
import { ProfileService } from '../profiles/profile.service'; // FIXME barrel doesn't work here
import { JhiLanguageHelper, Principal, LoginModalService, LoginService } from '../../shared';

import { VERSION, DEBUG_INFO_ENABLED } from '../../app.constants';
import { BankAccountService } from '../../entities/bank-account/bank-account.service';
import { BankAccount } from '../../entities/bank-account/bank-account.model';

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: [
        'navbar.scss'
    ]
})
export class NavbarComponent implements OnInit {

    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    eventSubscriber: Subscription;

    bankAccounts: BankAccount[];

    constructor(
        private loginService: LoginService,
        private languageHelper: JhiLanguageHelper,
        private languageService: JhiLanguageService,
        private principal: Principal,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private router: Router,
        private bankAccountService: BankAccountService,
        private eventManager: EventManager
    ) {
        this.version = DEBUG_INFO_ENABLED ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        this.languageService.addLocation('home');
    }

    ngOnInit() {
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });

        this.profileService.getProfileInfo().subscribe(profileInfo => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });

        this.loadBankAccounts();
        this.eventSubscriber = this.eventManager.subscribe('authenticationSuccess', (response) => this.loadBankAccounts());
    }

    loadBankAccounts() {
        this.bankAccountService.query().subscribe(res => {
            this.bankAccounts = res.json();
        });
    }

    changeLanguage(languageKey: string) {
      this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }
}
