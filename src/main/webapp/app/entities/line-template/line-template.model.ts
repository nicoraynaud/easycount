export class LineTemplate {
    constructor(
        public id?: number,
        public date?: any,
        public label?: string,
        public debit?: number,
        public credit?: number,
        public balance?: number,
        public categoriesId?: number,
        public bankAccountId?: number,
    ) {
    }
}
