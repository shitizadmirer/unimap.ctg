package ro.ulbsibiu.acaps.viewer.noc;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;

import ro.ulbsibiu.acaps.ctg.xml.mapping.MapType;
import ro.ulbsibiu.acaps.ctg.xml.mapping.MappingType;

import com.jgraph.components.labels.CellConstants;
import com.jgraph.components.labels.MultiLineVertexRenderer;

/**
 * {@link JGraphModelAdapter} for the Network-on-Chip topology
 * 
 * @author cipi
 * 
 */
public class NocJGraphModelAdapter extends JGraphModelAdapter<Object, Object> {

	/** autogenerated serial version UID */
	private static final long serialVersionUID = -7429662661307611647L;

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(NocJGraphModelAdapter.class);

	private File mappingXmlFile;

	public NocJGraphModelAdapter(Graph<Object, Object> jGraphTGraph,
			File mappingXmlFile) throws JAXBException {
		super(jGraphTGraph);
		this.mappingXmlFile = mappingXmlFile;
		logger.assertLog(mappingXmlFile != null, "Invalid mapping XML file!");

		addCoresToNodes();
	}

	private void addCoresToNodes() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext
				.newInstance("ro.ulbsibiu.acaps.ctg.xml.mapping");
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		@SuppressWarnings("unchecked")
		MappingType mappingType = ((JAXBElement<MappingType>) unmarshaller
				.unmarshal(mappingXmlFile)).getValue();
		logger.assertLog(mappingType != null, "No mapping provided!");
		List<MapType> mapList = mappingType.getMap();
		for (int i = 0; i < mapList.size(); i++) {
			MapType mapType = mapList.get(i);
			// add IP core to NoC node
			DefaultGraphCell cell = new DefaultGraphCell(mapType.getCore());
			getVertexCell(mapType.getNode()).add(cell);
		}
	}

	@Override
	public AttributeMap getDefaultEdgeAttributes() {
		AttributeMap map = new AttributeMap();

		GraphConstants.setEndFill(map, true);
		GraphConstants.setLabelEnabled(map, false);

		GraphConstants.setForeground(map, Color.decode("#25507C"));
		// GraphConstants.setFont(map,
		// GraphConstants.DEFAULTFONT.deriveFont(Font.BOLD, 12));
		GraphConstants.setLineColor(map, Color.decode("#7AA1E6"));

		return map;
	}

	@Override
	public AttributeMap getDefaultVertexAttributes() {
		AttributeMap map = new AttributeMap();

		// we set the bounds only to force the layout manager to use minimum
		// size shapes
		// this way the auto sizing mechanism will immediately generate the
		// optimum sized shapes
		// TODO I am not sure how exactly setBounds(...) works with
		// setAutoSize(...)
		GraphConstants.setBounds(map, new Rectangle2D.Double(0, 0, 100, 100));
		GraphConstants.setAutoSize(map, true);
		GraphConstants.setBackground(map, Color.LIGHT_GRAY);
		GraphConstants.setForeground(map, Color.WHITE);
		GraphConstants.setOpaque(map, true);
		GraphConstants.setGroupOpaque(map, true);
		GraphConstants.setFont(map,
				GraphConstants.DEFAULTFONT.deriveFont(Font.PLAIN, 4));
		GraphConstants.setBorderColor(map, Color.LIGHT_GRAY);
		GraphConstants.setHorizontalAlignment(map, JLabel.LEFT);
		GraphConstants.setVerticalAlignment(map, JLabel.BOTTOM);

		// we want to have the nodes represented as rectangles
		CellConstants.setVertexShape(map,
				MultiLineVertexRenderer.SHAPE_RECTANGLE);

		return map;
	}

}