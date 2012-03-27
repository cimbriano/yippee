package com.yippee.db.util;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.yippee.db.model.DocAug;

/**
 * Data access layer class: it provides simplified access to data stored in persistent storage (BerkeleyDB). It provides
 * a layer of abstraction to the managers
 */
public class DAL {
    // DocAug Accessors
    PrimaryIndex<String, DocAug> docById;

    /**
     * Data access layer constructor
     *
     * @param store the BerkeleyDB Entity store managing persistent entity objects.
     * @throws DatabaseException
     */
    public DAL(EntityStore store) throws DatabaseException {
        // Primary key for Inventory classes
        docById = store.getPrimaryIndex(String.class, DocAug.class);
        
    }

    /**
     * We need a getter to access the DocAug Primary index outside the package.
     *
     * @return the userByName index
     */
    public PrimaryIndex<String, DocAug> getDocById() {
        return docById;
    }
}