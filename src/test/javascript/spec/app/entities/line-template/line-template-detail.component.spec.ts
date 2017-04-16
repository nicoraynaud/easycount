import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { EasycountTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LineTemplateDetailComponent } from '../../../../../../main/webapp/app/entities/line-template/line-template-detail.component';
import { LineTemplateService } from '../../../../../../main/webapp/app/entities/line-template/line-template.service';
import { LineTemplate } from '../../../../../../main/webapp/app/entities/line-template/line-template.model';

describe('Component Tests', () => {

    describe('LineTemplate Management Detail Component', () => {
        let comp: LineTemplateDetailComponent;
        let fixture: ComponentFixture<LineTemplateDetailComponent>;
        let service: LineTemplateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EasycountTestModule],
                declarations: [LineTemplateDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LineTemplateService,
                    EventManager
                ]
            }).overrideComponent(LineTemplateDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LineTemplateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LineTemplateService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new LineTemplate(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.lineTemplate).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
