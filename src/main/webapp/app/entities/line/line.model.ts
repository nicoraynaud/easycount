
const enum LineStatus {
    'NEW',
    'TICKED',
    'CANCELLED'

};

const enum LineSource {
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
        public categoriesId?: number,
        public bankAccountId?: number,
        public bankAccountLabel?: string,
    ) { }
}
