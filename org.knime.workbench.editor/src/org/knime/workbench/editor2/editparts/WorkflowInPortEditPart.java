/*
 * ------------------------------------------------------------------------
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
 * -------------------------------------------------------------------
 *
 * History
 *   22.01.2008 (Fabian Dill): created
 */
package org.knime.workbench.editor2.editparts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.knime.core.node.port.PortType;
import org.knime.core.node.workflow.WorkflowInPort;
import org.knime.core.ui.node.workflow.ConnectionContainerUI;
import org.knime.core.ui.node.workflow.NodeContainerUI;
import org.knime.core.ui.node.workflow.NodeOutPortUI;
import org.knime.core.ui.node.workflow.WorkflowInPortUI;
import org.knime.core.ui.node.workflow.WorkflowManagerUI;
import org.knime.workbench.editor2.WorkflowContextMenuProvider;
import org.knime.workbench.editor2.WorkflowEditorMode;
import org.knime.workbench.editor2.figures.NewToolTipFigure;
import org.knime.workbench.editor2.figures.WorkflowInPortFigure;
import org.knime.workbench.editor2.model.WorkflowPortBar;

/**
 * Edit part representing a {@link WorkflowInPort}.
 * Model: {@link WorkflowInPort}
 * View: {@link WorkflowInPortFigure}
 * Controller: {@link WorkflowInPortEditPart}
 *
 * @author Fabian Dill, University of Konstanz
 */
public class WorkflowInPortEditPart extends AbstractPortEditPart {
    private static final String PORT_NAME = "Workflow In Port";

    private boolean m_isSelected = false;

    /**
     *
     * @param type port type
     * @param portID port id
     */
    public WorkflowInPortEditPart(final PortType type, final int portID) {
        super(type, portID, true);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    protected void refreshVisuals() {
        IFigure fig = getFigure();
        fig.setBounds(new Rectangle(new Point(0, 50), new Dimension(30, 30)));
        super.refreshVisuals();
    }



    /**
     * Convenience, returns the hosting container.
     *
     * {@inheritDoc}
     */
    @Override
    protected final NodeContainerUI getNodeContainer() {
        if (getParent() == null) {
            return null;
        }
        // if the referring WorkflowManager is displayed as a metanode, then
        // the parent is a NodeContainerEditPart
        if (getParent() instanceof NodeContainerEditPart) {
            return (NodeContainerUI)getParent().getModel();
        }
        // if the referring WorkflowManager is the "root" workflow manager of
        // the open editor then the parent is a WorkflowRootEditPart
        return ((WorkflowPortBar)getParent().getModel()).getWorkflowManager();
    }

    /**
     * Convenience, returns the WFM.
     *
     * {@inheritDoc}
     */
    @Override
    protected final WorkflowManagerUI getManager() {
        return (WorkflowManagerUI)getNodeContainer();
    }

    /**
     * {@inheritDoc}
     *
     * Creates {@link WorkflowInPortFigure}, sets the tooltip and adds a {@link MouseListener} to the figure in order to
     * detect if the figure was clicked and a context menu entry should be provided to open the port view.
     *
     * @see WorkflowContextMenuProvider#buildContextMenu( org.eclipse.jface.action.IMenuManager)
     * @see WorkflowInPortFigure
     */
    @Override
    protected IFigure createFigure() {
        final NodeOutPortUI port = getManager().getInPort(getIndex()).getUnderlyingPort();
        final String tooltip = getTooltipText(PORT_NAME + ": " + getIndex(), port);
        final WorkflowInPortFigure f =
            new WorkflowInPortFigure(getType(), getManager().getNrInPorts(), getIndex(), tooltip);
        f.addMouseListener(new MouseListener() {

            @Override
            public void mouseDoubleClicked(final MouseEvent me) { }

            /**
             * {@inheritDoc}
             *
             * Set the selection state of the figure to true. This is evaluated in the context menu. If it is selected a
             * context menu entry is provided to open the port view.
             *
             * @see WorkflowContextMenuProvider#buildContextMenu( org.eclipse.jface.action.IMenuManager)
             */
            @Override
            public void mousePressed(final MouseEvent me) {
                if (WorkflowEditorMode.NODE_EDIT.equals(m_currentEditorMode)) {
                    setSelected(true);
                }
            }

            /**
             * {@inheritDoc}
             *
             * Set the selection state of the figure to true. This is evaluated in the context menu. If it is selected a
             * context menu entry is provided to open the port view.
             *
             * @see WorkflowContextMenuProvider#buildContextMenu( org.eclipse.jface.action.IMenuManager)
             */
            @Override
            public void mouseReleased(final MouseEvent me) {
                if (WorkflowEditorMode.NODE_EDIT.equals(m_currentEditorMode)) {
                    setSelected(false);
                }
            }

        });
        return f;
    }

    /**
     * The context menu ({@link WorkflowContextMenuProvider#buildContextMenu(
     * org.eclipse.jface.action.IMenuManager)}) reads and resets the selection
     * state. This state is read by the mouse listener added in
     * {@link #createFigure()}.
     *
     * @return true if the underlying workflow in port figure was clicked, false
     *  otherwise
     * @see WorkflowContextMenuProvider#buildContextMenu(
     *  org.eclipse.jface.action.IMenuManager)
     *
     */
    public boolean isSelected() {
        return m_isSelected;
    }

    /**
     * The context menu ({@link WorkflowContextMenuProvider#buildContextMenu(
     * org.eclipse.jface.action.IMenuManager)}) reads and resets the selection
     * state. This state is set by the mouse listener added in
     * {@link #createFigure()}.
     *
     * @param isSelected true if the figure was clicked, false otherwise.
     *
     * @see WorkflowContextMenuProvider#buildContextMenu(
     *  org.eclipse.jface.action.IMenuManager)
     */
    public void setSelected(final boolean isSelected) {
        m_isSelected = isSelected;
    }

    /**
     * Returns the connections that has this workflow in-port as a source.
     *
     * @return list containing the connections, or an empty list. Never
     *         <code>null</code>
     *
     * @see org.eclipse.gef.GraphicalEditPart#getTargetConnections()
     */
    @Override
    public List<ConnectionContainerUI> getModelSourceConnections() {
        if (getManager() == null) {
            return EMPTY_LIST;
        }
        Set<ConnectionContainerUI> containers =
                getManager().getOutgoingConnectionsFor(
                        getNodeContainer().getID(),
                        getIndex());
        List<ConnectionContainerUI>conns = new ArrayList<ConnectionContainerUI>();
        if (containers != null) {
            conns.addAll(containers);
        }
        return conns;
    }

    /**
     *
     * @return empty list, as workflow in ports are never target for connections
     *
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart
     *      #getModelSourceConnections()
     */
    @Override
    protected List<ConnectionContainerUI> getModelTargetConnections() {
        return EMPTY_LIST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rebuildTooltip() {
        NodeOutPortUI port = ((WorkflowInPortUI)getNodeContainer().getInPort(
                getIndex())).getUnderlyingPort();
        String tooltip = getTooltipText(PORT_NAME + ": " + getIndex(), port);
        ((NewToolTipFigure)getFigure().getToolTip()).setText(tooltip);
    }



}
