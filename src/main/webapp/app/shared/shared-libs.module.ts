import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgJhipsterModule } from 'ng-jhipster';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
    imports: [
        NgbModule.forRoot(),
        NgJhipsterModule.forRoot({
            i18nEnabled: true,
            defaultI18nLocation: 'all',
            defaultI18nLang: 'fr'
        }),
        InfiniteScrollModule,
        NgxChartsModule,
        BrowserAnimationsModule
    ],
    exports: [
        FormsModule,
        HttpModule,
        CommonModule,
        NgbModule,
        NgJhipsterModule,
        InfiniteScrollModule,
        NgxChartsModule,
        BrowserAnimationsModule
    ]
})
export class EasycountSharedLibsModule {}
