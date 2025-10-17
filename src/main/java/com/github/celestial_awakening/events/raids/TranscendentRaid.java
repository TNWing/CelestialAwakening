package com.github.celestial_awakening.events.raids;

public class TranscendentRaid extends AbstractCARaid{
    /*
    the main issue w/ current implementation is if there are multiple targets with celestial beacon. this can cause a lot of lag and make raids unfair

    if i use this class, i can do it like this
    for each former spawn interval (so 60 sec for tier 1 beacon, 30 sec for tier 2 beacon), inform the serverlevel of the spawn interval.
    serverlevel checks the raid map and sees if there are any transcendent raids near that area. if so, add a set amount of points to it.
    points will upgrade the raid strength if there is enough
    ex:
    Raid tiers
    1: no pt req
    2: 100 pts
    3: 250 pts

    Spawn Interval Tiers
    1: 25 pts
    2: 75 pts
    3: 150 pts
     */
    /*
Data to store
-raid id
-delay before start
-raid strength
-raid primary targets (player uuids)

Important notes
-vanilla illagers store the raid ids, in order to associate themselves with raids. when said illager dies, it informs the raid of its death


 */


}
