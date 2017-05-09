import {Pipe} from '@angular/core';

@Pipe({
    name: 'currencyFormat'
})
export class CurrencyFormatPipe {
    transform(value: number,
              currencySign = '€ ',
              decimalLength = 2,
              chunkDelimiter = '.',
              decimalDelimiter = ',',
              chunkLength = 3): string {

        if (!value) {
            return '';
        }

        const result = '\\d(?=(\\d{' + chunkLength + '})+' + (decimalLength > 0 ? '\\D' : '$') + ')';
        const num = value.toFixed(Math.max(0, decimalLength));

        return (decimalDelimiter ? num.replace('.', decimalDelimiter) : num).replace(new RegExp(result, 'g'), '$&' + chunkDelimiter) + ' ' + currencySign;
    }
}
