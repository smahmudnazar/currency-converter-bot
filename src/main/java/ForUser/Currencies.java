package ForUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currencies {
    USD(1),EUR(2),RUB(3),UZS(4),GBP(5),CHF(6),CNY(7),KGS(8),KZT(9),SAR(10),TRY(11);

    private final int id;
}
