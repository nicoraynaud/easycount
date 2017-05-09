import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { MockBackend } from '@angular/http/testing';
import { Http, BaseRequestOptions } from '@angular/http';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils } from 'ng-jhipster';
import { JhiLanguageService } from 'ng-jhipster';
import { MockLanguageService } from '../../../helpers/mock-language.service';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CurrencyDetailComponent } from '../../../../../../main/webapp/app/entities/currency/currency-detail.component';
import { CurrencyService } from '../../../../../../main/webapp/app/entities/currency/currency.service';
import { Currency } from '../../../../../../main/webapp/app/entities/currency/currency.model';

describe('Component Tests', () => {

    describe('Currency Management Detail Component', () => {
        let comp: CurrencyDetailComponent;
        let fixture: ComponentFixture<CurrencyDetailComponent>;
        let service: CurrencyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [CurrencyDetailComponent],
                providers: [
                    MockBackend,
                    BaseRequestOptions,
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    {
                        provide: Http,
                        useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
                            return new Http(backendInstance, defaultOptions);
                        },
                        deps: [MockBackend, BaseRequestOptions]
                    },
                    {
                        provide: JhiLanguageService,
                        useClass: MockLanguageService
                    },
                    CurrencyService
                ]
            }).overrideComponent(CurrencyDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CurrencyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CurrencyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Currency(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.currency).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
