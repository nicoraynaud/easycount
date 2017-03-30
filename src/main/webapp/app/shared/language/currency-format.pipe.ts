import {Pipe} from '@angular/core';

@Pipe({
    name: 'currencyFormat'
})
export class CurrencyFormatPipe {
    transform(value: number,
              currencySign: string = '€ ',
              decimalLength: number = 2,
              chunkDelimiter: string = '.',
              decimalDelimiter:string = ',',
              chunkLength: number = 3): string {

        if (!value) {
            return ""
        }

        let result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')'
        let num = value.toFixed(Math.max(0, ~~decimalLength));

        return (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter) + ' ' + currencySign;
    }
}
