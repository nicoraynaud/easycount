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
    BankAccountImportLinesPopupComponent
} from './';

import {EasycountLineModule} from '../line/line.module';

let ENTITY_STATES = [
    ...bankAccountRoute,
    ...bankAccountPopupRoute,
];

@NgModule({
    imports: [
        EasycountSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        EasycountLineModule
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
        BankAccountImportLinesPopupComponent
    ],
    entryComponents: [
        BankAccountComponent,
        BankAccountDialogComponent,
        BankAccountPopupComponent,
        BankAccountDeleteDialogComponent,
        BankAccountDeletePopupComponent,
        BankAccountImportLinesDialogComponent,
        BankAccountImportLinesPopupComponent
    ],
    providers: [
        BankAccountService,
        BankAccountPopupService,
        BankAccountResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EasycountBankAccountModule {}
