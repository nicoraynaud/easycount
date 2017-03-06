import {Component, Input} from '@angular/core';
import {JhiLanguageService, AlertService, EventManager} from 'ng-jhipster';
import { Line } from './line.model';
import { LineService } from './line.service';
import {CategoryService} from '../category/category.service';
import {Response} from '@angular/http';

@Component({
    selector: '[jhi-line-row]',
    templateUrl: './line-row.component.html'
})
export class LineRowComponent {

    @Input('jhi-line-row')
    line: Line;

    isSaving: boolean;
    isEditing: boolean;

    constructor(
        private alertService: AlertService,
        private categoryService: CategoryService,
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private lineService: LineService
    ) {
        this.jhiLanguageService.addLocation('line');
    }

    cancel () {
        // Cancel only if this is not a new line (without an id)
        if (this.line.id) {
            this.lineService.find(this.line.id).subscribe(line => {
                this.line = line;
            });
        }
    }

    save () {
        this.isSaving = true;
        if (this.line.id !== undefined) {
            this.lineService.update(this.line)
                .subscribe((res: Line) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        } else {
            this.lineService.create(this.line)
                .subscribe((res: Line) => this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
        }
    }

    toggleEdit() {
        this.isEditing = !this.isEditing;
    }

    private onSaveSuccess (result: Line) {
        this.eventManager.broadcast({ name: 'lineListModification', content: 'OK'});
        this.isSaving = false;
    }

    private onSaveError (error) {
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

}
