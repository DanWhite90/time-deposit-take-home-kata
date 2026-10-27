-- Sample plans so the endpoints can be tried from Swagger UI straight after `docker compose up`.
-- Assumption: the take-home has no other way to create plans (exactly two endpoints are allowed), so demo data
-- ships as a migration. A real deployment would keep reference or demo data out of schema migrations.
-- One row per interest rule boundary. Expected balance after one POST /time-deposits/balances is in brackets.
INSERT INTO "timeDeposits" (id, "planType", days, balance) VALUES
    (1, 'basic',   45,  1234567.00), -- 1% / 12        [1235595.81]
    (2, 'basic',   30,     5000.00), -- first 30 days  [5000.00]
    (3, 'student', 200,    2400.00), -- 3% / 12        [2406.00]
    (4, 'student', 366,    2400.00), -- after 1 year   [2400.00]
    (5, 'premium', 45,    12000.00), -- until day 45   [12000.00]
    (6, 'premium', 90,    12000.00)  -- 5% / 12        [12050.00]
ON CONFLICT (id) DO NOTHING;

INSERT INTO withdrawals (id, "timeDepositId", amount, date) VALUES
    (1, 1, 250.00, '2026-01-15'),
    (2, 1,  75.50, '2026-03-10'),
    (3, 3, 100.00, '2026-02-01')
ON CONFLICT (id) DO NOTHING;
