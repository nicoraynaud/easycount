import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EasycountCategoryModule } from './category/category.module';
import { EasycountCurrencyModule } from './currency/currency.module';
import { EasycountBankModule } from './bank/bank.module';
import { EasycountBankAccountModule } from './bank-account/bank-account.module';
import { EasycountLineModule } from './line/line.module';
import { EasycountLineTemplateModule } from './line-template/line-template.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EasycountCategoryModule,
        EasycountCurrencyModule,
        EasycountBankModule,
        EasycountBankAccountModule,
        EasycountLineModule,
        EasycountLineTemplateModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EasycountEntityModule {}
