
import {Category} from "../category/category.model";
export const enum LineStatus {
    'NEW',
    'TICKED',
    'CANCELLED'

};

export const enum LineSource {
    'MANUAL',
    'GENERATED'

};
export class Line {
    constructor(
        public id?: number,
        public date?: any,
        public label?: string,
        public debit?: number,
        public credit?: number,
        public balance?: number,
        public status?: LineStatus,
        public source?: LineSource,
        public categories?: Category[],
        //public categoriesId?: number[],
        public bankAccountId?: number
    ) { }
}
