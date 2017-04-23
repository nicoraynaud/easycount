import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EasycountSharedModule } from '../../shared';

import {
    BankAccountService,
    BankAccountPopupService,
    BankAccountComponent,
    BankAccountDetailComponent,
    BankAccountDialogComponent,
    BankAccountDashboardComponent,
    BankAccountPopupComponent,
    BankAccountDeletePopupComponent,
    BankAccountDeleteDialogComponent,
    bankAccountRoute,
    bankAccountPopupRoute,
    BankAccountResolvePagingParams,
    BankAccountImportLinesDialogComponent,
    BankAccountImportLinesPopupComponent,
    BankAccountGenerateLinesDialogComponent,
    BankAccountGenerateLinesPopupComponent
} from './';

let ENTITY_STATES = [
    ...bankAccountRoute,
    ...bankAccountPopupRoute,
];

@NgModule({
    imports: [
        EasycountSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BankAccountComponent,
        BankAccountDetailComponent,
        BankAccountDashboardComponent,
        BankAccountDialogComponent,
        BankAccountDeleteDialogComponent,
        BankAccountPopupComponent,
        BankAccountDeletePopupComponent,
        BankAccountImportLinesDialogComponent,
        BankAccountImportLinesPopupComponent,
        BankAccountGenerateLinesDialogComponent,
        BankAccountGenerateLinesPopupComponent
    ],
    entryComponents: [
        BankAccountComponent,
        BankAccountDialogComponent,
        BankAccountPopupComponent,
        BankAccountDeleteDialogComponent,
        BankAccountDeletePopupComponent,
        BankAccountImportLinesDialogComponent,
        BankAccountImportLinesPopupComponent,
        BankAccountGenerateLinesDialogComponent,
        BankAccountGenerateLinesPopupComponent
    ],
    providers: [
        BankAccountService,
        BankAccountPopupService,
        BankAccountResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EasycountBankAccountModule {}
