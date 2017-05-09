import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EasycountSharedModule } from '../../shared';

import { TagInputModule } from 'ng2-tag-input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // this is needed!

import {
    LineService,
    LinePopupService,
    LineComponent,
    LineDetailComponent,
    LineDialogComponent,
    LinePopupComponent,
    LineDeletePopupComponent,
    LineDeleteDialogComponent,
    lineRoute,
    linePopupRoute,
    LineResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...lineRoute,
    ...linePopupRoute,
];

@NgModule({
    imports: [
        EasycountSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        TagInputModule,
        BrowserAnimationsModule
    ],
    declarations: [
        LineComponent,
        LineDetailComponent,
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
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EasycountLineModule {}
