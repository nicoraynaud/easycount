import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CurrencyComponent } from './currency.component';
import { CurrencyDetailComponent } from './currency-detail.component';
import { CurrencyPopupComponent } from './currency-dialog.component';
import { CurrencyDeletePopupComponent } from './currency-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CurrencyResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const currencyRoute: Routes = [
  {
    path: 'currency',
    component: CurrencyComponent,
    resolve: {
      'pagingParams': CurrencyResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.currency.home.title'
    }
  }, {
    path: 'currency/:id',
    component: CurrencyDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.currency.home.title'
    }
  }
];

export const currencyPopupRoute: Routes = [
  {
    path: 'currency-new',
    component: CurrencyPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.currency.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'currency/:id/edit',
    component: CurrencyPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.currency.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'currency/:id/delete',
    component: CurrencyDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.currency.home.title'
    },
    outlet: 'popup'
  }
];
