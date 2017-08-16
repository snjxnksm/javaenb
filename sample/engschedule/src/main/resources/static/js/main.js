$(function() {
  graph = new joint.dia.Graph;
  paper = new joint.dia.Paper({ 
    el: $('#paper'), 
   width: 1290, 
   height: 200, 
   gridSize: 20, 
   model: graph
  });

  // 初期から出ているbox
  var el1 = new joint.shapes.html.Element({
    position: { x: 10, y: 10 },
    size: { width: 150, height: 40 },
    divName: 'Atrae',
    isCompany: true
  });

  graph.addCells([el1]);

  paper.on('cell:pointerup', function(cellView, evt, x, y) {
    var elementBelow = graph.get('cells').find(function(cell) {
      if (cell instanceof joint.dia.Link) return false;
      if (cell.id === cellView.model.id) return false;
      if (cell.getBBox().containsPoint(g.point(x, y))) return true;
      return false;
    });                                                                                                                                                        

    if (elementBelow && !_.contains(graph.getNeighbors(elementBelow), cellView.model)) {
      graph.addCell(new joint.shapes.org.Arrow({
        source: { id: cellView.model.id },
        target: { id: elementBelow.id }
      }));
      cellView.model.translate(-200, 0);
    }
  });
});

joint.shapes.html = {};
joint.shapes.html.Element = joint.shapes.basic.Rect.extend({
  defaults: joint.util.deepSupplement({
    type: 'html.Element',
    attrs: {
      rect: { stroke: 'none', 'fill-opacity': 0 }
    }
  }, joint.shapes.basic.Rect.prototype.defaults)
});

joint.shapes.html.ElementView = joint.dia.ElementView.extend({
  template: [
    '<div class="html-element">',
    '<button class="delete">x</button>',
    '<input type="text" value=""/>',
    '<button class="js-divName"></button>',
    '<button class="add">＋</button>',
    '</div>'
  ].join(''),

  initialize: function() {
    _.bindAll(this, 'updateBox');
    joint.dia.ElementView.prototype.initialize.apply(this, arguments);

    this.$box = $(_.template(this.template)());
    this.$box.find('input').on('mousedown click', function(evt) { evt.stopPropagation(); });

    this.$box.find('input').on('change', _.bind(function(evt) {
      this.model.set('divName', $(evt.target).val());
    }, this));

    // delete element
    this.$box.find('.delete').on('click', _.bind(this.model.remove, this.model));
    this.$box.find('.add').on('click', _.bind(this.addBox, this.model));
    this.model.on('change', this.updateBox, this);
    this.model.on('remove', this.removeBox, this);

    if (this.model.get('isCompany') === true) {
      this.$box.find('input').remove();
      this.$box.find('.delete').remove();
    }
    this.updateBox();
  },
  render: function() {
    joint.dia.ElementView.prototype.render.apply(this, arguments);
    this.paper.$el.prepend(this.$box);
    this.updateBox();
    return this;
  },

 updateBox: function() {
    var bbox = this.model.getBBox();
    this.$box.find('.js-divName').text(this.model.get('divName'));
    this.$box.css({
      width: bbox.width,
      height: bbox.height,
      left: bbox.x,
      top: bbox.y,
      transform: 'rotate(' + (this.model.get('angle') || 0) + 'deg)'
    });
  },
  removeBox: function(evt) {
    this.$box.remove();
  },
  addBox: function(evt) {
    var el1 = new joint.shapes.html.Element({
      position: { x: 5, y: 5 },
      size: { width: 150, height: 40 },
      divName: ''
    });

    var line = new joint.shapes.org.Arrow({
      source: { id: this.id },
      target: { id: el1.id },
      vertices: [{ x: 100, y: 120 }, { x: 150, y: 60 }]
    });
    graph.addCells([el1, line]);
  }
});