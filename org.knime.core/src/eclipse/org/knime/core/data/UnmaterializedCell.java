/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   2 Aug 2018 (Marc Bux, KNIME AG, Zurich, Switzerland): created
 */
package org.knime.core.data;

import java.io.IOException;

/**
 * Implementation of an unmaterialized cell that has not been read and therefore cannot be accessed or worked with.
 *
 * @author Marc Bux, KNIME AG, Zurich, Switzerland
 * @since 3.6 // TODO bump to 3.7
 */
@SuppressWarnings("serial")
public final class UnmaterializedCell extends DataCell {
    static final UnmaterializedCell INSTANCE = new UnmaterializedCell();

    /**
     * Long term, we might introduce some kind of lazy loading of unmaterialized cells. For now, we simply throw an
     * exception when an unmaterialized cell is accessed in any way.
     */
    static final String ACCESS_EXCEPTION = "Data cell is not materialized and cannot be accessed.";

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        throw new UnsupportedOperationException(ACCESS_EXCEPTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean equalsDataCell(final DataCell dc) {
        throw new UnsupportedOperationException(ACCESS_EXCEPTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException(ACCESS_EXCEPTION);
    }

    /**
     * Factory for (de-)serializing an unmaterialized cell.
     *
     * @noreference This class is not intended to be referenced by clients.
     */
    public static final class UnmaterializedSerializer implements DataCellSerializer<UnmaterializedCell> {
        /** {@inheritDoc} */
        @Override
        public void serialize(final UnmaterializedCell cell, final DataCellDataOutput output) throws IOException {
            throw new UnsupportedOperationException(ACCESS_EXCEPTION);
        }

        /** {@inheritDoc} */
        @Override
        public UnmaterializedCell deserialize(final DataCellDataInput input) throws IOException {
            return INSTANCE;
        }

    }
}
