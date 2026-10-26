# Agent rules

Java 25, Spring Boot, hexagonal layout. Run `mvn verify` before claiming a task is done. There is no Maven wrapper.

## Assignment Constraints

- Leave `TimeDeposit.balance` as `Double`; the assignment model must stay unchanged, including
  `updateBalance(List<TimeDeposit>)`.
- For database keep camelCase table and column names. Quote them so PostgreSQL does not fold them to lowercase.

## Style

- Constructor injection only. Never use field `@Autowired`.
- DTOs are records. Never expose JPA entities in API responses.
- Use `final` on fields and parameters where possible.
- Return `Optional<T>` for a single-entity lookup. Do not return null.
- No `System.out`. Use SLF4J.
- On new code, Lombok is only `@Slf4j`. Do not add `@Data`.
- Use Lombok `*ArgsConstructor` annotations instead of explicit constructor definitions where possible.
- In functions bodies keep a blank line between initial variable declarations, ifs and loops, the main body of the 
  function and the return statement. 

## Java / Spring Boot

- Prefer Java 21+ features: records and pattern matching.
- Keep `@Transactional` on service methods only.
- Never swallow exceptions.
- Controllers call services or inbound use cases only, never repositories.
- Use `BigDecimal` for money.

## Persistence

- Flyway owns DDL. Hibernate `ddl-auto` stays `validate`.

## Testing

- Write tests using `@DisplayName` descriptions and hierarchical testing using `@Nested` classes.