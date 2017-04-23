
import {Category} from '../category/category.model';

export const enum TemplateFrequency {
    'MONTHLY',
    'BI_MONTHLY',
    'QUARTERLY',
    'YEARLY'

};
export class LineTemplate {
    constructor(
        public id?: number,
        public label?: string,
        public debit?: number,
        public credit?: number,
        public active?: boolean,
        public dayOfMonth?: number,
        public frequency?: TemplateFrequency,
        public startDate?: any,
        public occurrences?: number,
        public linesId?: number,
        public categories?: Category[],
        public bankAccountId?: number,
    ) {
        this.active = false;
    }
}
