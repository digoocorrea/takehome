
# TAKEHOME PROJECT

**Endpoint:** /load/transfers/{date}  _(Date: "2022-05-01")_

Recover all transfers from date informed to the current date on TUS contract token from Moralis DeepIndex API and saves the transfer events as MINT or BURN events on transferEvent collection.

If a previous load was in progress the service will pick up from the last transfer saved and continue from there until syncing the parameter date 

_The event type segmentation follows these rules:_
- _A mint is anytime a token has been sent from a 0x0 address to any other address_
- _A burn is anytime a token is sent to 0x0 address OR Crabada’s breeding fee address:_
_0x4e57a39cac2499abeafd3698f7164ecbfde008ee_

- _Any tokens sent from these two address to the 0x0 address are not
counted towards the burn rate:_
_0xb5a0ffe202e9223dce018c4fdc9e6b952fac4a2c_
_0x4e57a39cac2499abeafd3698f7164ecbfde008ee_

.

**Endpoint:** /load/mint

Aggregates all MINT transfer events from transferEvent collection and summarize on a per-day basis on mintRate collection. 

.

**Endpoint:** /load/burn

Aggregates all BURN transfer events from transferEvent collection and summarize on a per-day basis on burnRate collection.

.

**Endpoint:** /analytics/inflation

Compile data from mintRate and burnRate collections and returns the inflation rates on a per-day basis
