Natural Language Processing
===
This (ongoing) project estimates a language model for Farsi (Persian). It fetches online sources, parse them into trigram word sequences and estimate a Markov language model.

The program has state. The current fetched sources and the estimation is persisted in SQLite. Upon each run it accumulates further language sources, parse more entities, and adds to the accuracy of the estimation.

Operations
---
1. Fetch persian news posts
2. Store them to disc
3. Restore the unparsed posts
4. Parse the posts
5. Store the parsed word sequences to disc
6. Estimate Trigram model with linear interpolation

In order to boost the running performance, the operations are implemented asynchronously.
