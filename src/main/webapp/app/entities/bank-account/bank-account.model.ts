
const enum BankAccountType {
    'CHECKING',
    'SAVINGS',
    'OTHER'

};
export class BankAccount {
    constructor(
        public id?: number,
        public label?: string,
        public number?: string,
        public type?: BankAccountType,
        public linesId?: number,
        public bankId?: number,
        public currencyId?: number,
    ) {
    }
}
