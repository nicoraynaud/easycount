import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EasycountSharedModule } from '../../shared';

import {
    CurrencyService,
    CurrencyPopupService,
    CurrencyComponent,
    CurrencyDetailComponent,
    CurrencyDialogComponent,
    CurrencyPopupComponent,
    CurrencyDeletePopupComponent,
    CurrencyDeleteDialogComponent,
    currencyRoute,
    currencyPopupRoute,
    CurrencyResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...currencyRoute,
    ...currencyPopupRoute,
];

@NgModule({
    imports: [
        EasycountSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CurrencyComponent,
        CurrencyDetailComponent,
        CurrencyDialogComponent,
        CurrencyDeleteDialogComponent,
        CurrencyPopupComponent,
        CurrencyDeletePopupComponent,
    ],
    entryComponents: [
        CurrencyComponent,
        CurrencyDialogComponent,
        CurrencyPopupComponent,
        CurrencyDeleteDialogComponent,
        CurrencyDeletePopupComponent,
    ],
    providers: [
        CurrencyService,
        CurrencyPopupService,
        CurrencyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EasycountCurrencyModule {}
