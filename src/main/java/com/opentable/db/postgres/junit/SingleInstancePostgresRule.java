/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opentable.db.postgres.junit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.rules.ExternalResource;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;

public class SingleInstancePostgresRule extends ExternalResource
{
    private volatile EmbeddedPostgres epg;
    private volatile Connection postgresConnection;

    SingleInstancePostgresRule() { }

    @Override
    protected void before() throws Throwable
    {
        super.before();
        epg = EmbeddedPostgres.start();
        postgresConnection = epg.getPostgresDatabase().getConnection();
    }

    public EmbeddedPostgres getEmbeddedPostgres()
    {
        EmbeddedPostgres epg = this.epg;
        if (epg == null) {
            throw new AssertionError("JUnit test not started yet!");
        }
        return epg;
    }

    @Override
    protected void after()
    {
        try {
            postgresConnection.close();
        } catch (SQLException e) {
            throw new AssertionError(e);
        }
        try {
            epg.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
