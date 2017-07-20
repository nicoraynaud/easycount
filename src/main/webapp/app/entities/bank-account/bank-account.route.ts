import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes} from "@angular/router";
import {PaginationUtil} from "ng-jhipster";

import {BankAccountComponent} from "./bank-account.component";
import {BankAccountDetailComponent} from "./bank-account-detail.component";
import {BankAccountPopupComponent} from "./bank-account-dialog.component";
import {BankAccountDeletePopupComponent} from "./bank-account-delete-dialog.component";
import {BankAccountDashboardComponent} from "./bank-account-dashboard.component";
import {BankAccountImportLinesPopupComponent} from "./bank-account-import-lines-dialog.component";
import {BankAccountGenerateLinesPopupComponent} from "./bank-account-generate-lines-dialog.component";
import {BankAccountChartsComponent} from './bank-account-charts.component';

@Injectable()
export class BankAccountResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: PaginationUtil) {
    }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
        };
    }
}

export const bankAccountRoute: Routes = [
    {
        path: 'bank-account',
        component: BankAccountComponent,
        resolve: {
            'pagingParams': BankAccountResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        }
    },
    {
        path: 'bank-account/:id/dashboard',
        component: BankAccountDashboardComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        }
    },
    {
        path: 'bank-account/:id/charts',
        component: BankAccountChartsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.charts.title'
        }
    },
    {
        path: 'bank-account/:id',
        component: BankAccountDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        }
    }
];

export const bankAccountPopupRoute: Routes = [
    {
        path: 'bank-account-new',
        component: BankAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        },
        outlet: 'popup'
    },
    {
        path: 'bank-account/:id/edit',
        component: BankAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        },
        outlet: 'popup'
    },
    {
        path: 'bank-account/:id/import-lines',
        component: BankAccountImportLinesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        },
        outlet: 'popup'
    },
    {
        path: 'bank-account/:id/generate-lines',
        component: BankAccountGenerateLinesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        },
        outlet: 'popup'
    },
    {
        path: 'bank-account/:id/delete',
        component: BankAccountDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'easycountApp.bankAccount.home.title'
        },
        outlet: 'popup'
    }
];
