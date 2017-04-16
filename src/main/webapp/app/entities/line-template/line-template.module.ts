import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EasycountSharedModule } from '../../shared';
import {
    LineTemplateService,
    LineTemplatePopupService,
    LineTemplateComponent,
    LineTemplateDetailComponent,
    LineTemplateDialogComponent,
    LineTemplatePopupComponent,
    LineTemplateDeletePopupComponent,
    LineTemplateDeleteDialogComponent,
    lineTemplateRoute,
    lineTemplatePopupRoute,
    LineTemplateResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...lineTemplateRoute,
    ...lineTemplatePopupRoute,
];

@NgModule({
    imports: [
        EasycountSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        LineTemplateComponent,
        LineTemplateDetailComponent,
        LineTemplateDialogComponent,
        LineTemplateDeleteDialogComponent,
        LineTemplatePopupComponent,
        LineTemplateDeletePopupComponent,
    ],
    entryComponents: [
        LineTemplateComponent,
        LineTemplateDialogComponent,
        LineTemplatePopupComponent,
        LineTemplateDeleteDialogComponent,
        LineTemplateDeletePopupComponent,
    ],
    providers: [
        LineTemplateService,
        LineTemplatePopupService,
        LineTemplateResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EasycountLineTemplateModule {}
