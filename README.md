
# TAKEHOME PROJECT

**Endpoint:** /load/transfers/{date}/{page}  _(Date: "2022-05-01", Integer: 0)_

Recover all transfers on TUS contract token from Moralis DeepIndex API and saves the transfer events as MINT or BURN events on transferEvent collection.

.

**Endpoint:** /load/mint

Aggregates all MINT transfer events from transferEvent collection and summarize on per day basis on mintRate collection. 

.

**Endpoint:** /load/burn

Aggregates all BURN transfer events from transferEvent collection and summarize on per day basis on burnRate collection.

.

**Endpoint:** /analytics/inflation

Compile data from mintRate and burnRate collections and returns the inflation rates on per day basis