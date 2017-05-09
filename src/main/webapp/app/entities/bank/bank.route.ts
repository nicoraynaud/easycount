import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { BankComponent } from './bank.component';
import { BankDetailComponent } from './bank-detail.component';
import { BankPopupComponent } from './bank-dialog.component';
import { BankDeletePopupComponent } from './bank-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class BankResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

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

export const bankRoute: Routes = [
  {
    path: 'bank',
    component: BankComponent,
    resolve: {
      'pagingParams': BankResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.bank.home.title'
    }
  }, {
    path: 'bank/:id',
    component: BankDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.bank.home.title'
    }
  }
];

export const bankPopupRoute: Routes = [
  {
    path: 'bank-new',
    component: BankPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.bank.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'bank/:id/edit',
    component: BankPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.bank.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'bank/:id/delete',
    component: BankDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.bank.home.title'
    },
    outlet: 'popup'
  }
];
