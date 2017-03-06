import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EasycountSharedModule } from '../../shared';

import {
    LineService,
    LinePopupService,
    LineComponent,
    LineDetailComponent,
    LineRowComponent,
    LineDialogComponent,
    LinePopupComponent,
    LineDeletePopupComponent,
    LineDeleteDialogComponent,
    lineRoute,
    linePopupRoute,
    LineResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...lineRoute,
    ...linePopupRoute,
];

@NgModule({
    imports: [
        EasycountSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        LineComponent,
        LineDetailComponent,
        LineRowComponent,
        LineDialogComponent,
        LineDeleteDialogComponent,
        LinePopupComponent,
        LineDeletePopupComponent,
    ],
    entryComponents: [
        LineComponent,
        LineDialogComponent,
        LinePopupComponent,
        LineDeleteDialogComponent,
        LineDeletePopupComponent,
    ],
    providers: [
        LineService,
        LinePopupService,
        LineResolvePagingParams,
    ],
    exports: [
        LineRowComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EasycountLineModule {}
