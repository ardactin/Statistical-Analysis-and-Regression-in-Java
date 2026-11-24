# Statistical Analysis & Linear Regression in Java

A dependency-free Java program that generates a synthetic dataset, finds the feature most correlated with the target, fits a simple linear regression model on it and evaluates the model on a held-out test set.

## What It Does

1. Generates random data with three features (`x1`, `x2`, `x3`) and a noisy target `y = 2·x1 + 3·x2 − x3 + noise`
2. Splits the data into training and test sets
3. Computes the **Pearson correlation** of each feature with `y`
4. Picks the strongest feature and fits `y = b0 + b1·x` with **least squares**
5. Reports the **SSE** (sum of squared errors) on training and test data

## Build & Run

```bash
javac LinearRegression.java
java LinearRegression
```

## Example Output

```text
Korelasyon Katsayıları:
r1 (x1-y): 0.5602
r2 (x2-y): 0.8167
r3 (x3-y): -0.2724

En yüksek korelasyon: x2 (0.8167)

Regresyon Modeli: y = 40.4561 + 3.1354 * x2

Eğitim Verileri SSE: 256302.3332
Test Verileri SSE: 181575.1963
```

Values change on each run because the data is random.

## Contact

- Email: [ctin.arda@gmail.com](mailto:ctin.arda@gmail.com)
- LinkedIn: [linkedin.com/in/arda-çetin-a527692a7](https://www.linkedin.com/in/arda-%C3%A7etin-a527692a7/)
