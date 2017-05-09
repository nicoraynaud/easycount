import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { LineTemplateComponent } from './line-template.component';
import { LineTemplateDetailComponent } from './line-template-detail.component';
import { LineTemplatePopupComponent } from './line-template-dialog.component';
import { LineTemplateDeletePopupComponent } from './line-template-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class LineTemplateResolvePagingParams implements Resolve<any> {

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

export const lineTemplateRoute: Routes = [
  {
    path: 'line-template',
    component: LineTemplateComponent,
    resolve: {
      'pagingParams': LineTemplateResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.lineTemplate.home.title'
    }
  }, {
    path: 'line-template/:id',
    component: LineTemplateDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.lineTemplate.home.title'
    }
  }
];

export const lineTemplatePopupRoute: Routes = [
  {
    path: 'line-template-new',
    component: LineTemplatePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.lineTemplate.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'line-template/:id/edit',
    component: LineTemplatePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.lineTemplate.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'line-template/:id/delete',
    component: LineTemplateDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'easycountApp.lineTemplate.home.title'
    },
    outlet: 'popup'
  }
];
