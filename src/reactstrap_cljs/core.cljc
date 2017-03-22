(ns reactstrap-cljs.core
  (:refer-clojure :exclude [use mask])
  (:require #?(:cljs cljsjs.reactstrap)
            #?(:cljs cljsjs.react)
            [clojure.string :as str]
            [om-tools.dom :as omt]))

(defn kebab-case
  "Converts CamelCase / camelCase to kebab-case"
  [s]
  (str/join "-" (map str/lower-case (re-seq #"\w[a-z]+" s))))

(def bootstrap-tags
  '[Alert
    Badge
    Breadcrumb
    BreadcrumbItem
    Button
    ButtonGroup
    ButtonDropdown
    UncontrolledButtonDropdown
    Dropdown
    DropdownToggle
    DropdownMenu
    DropdownItem
    Card
    CardImg
    CardBlock
    CardTitle
    CardSubtitle
    CardText
    CardLink
    CardHeader
    CardFooter
    CardImgOverlay
    CardGroup
    CardDeck
    CardColumns
    Collapse
    Form
    FormGroup
    FormText
    FormFeedback
    Label
    Input
    InputGroup
    InputGroupAddon
    InputGroupButton
    InputGroupButton
    Jumbotron
    Container
    Row
    Col
    ListGroup
    ListGroupItem
    Badge
    ListGroupItemHeading
    ListGroupItemText
    Media
    Modal
    ModalHeader
    ModalBody
    ModalFooter
    Navbar
    NavbarToggler
    NavbarBrand
    Nav
    NavItem
    NavLink
    NavDropdown
    UncontrolledNavDropdown
    Pagination
    PaginationItem
    PaginationLink
    Popover
    PopoverTitle
    PopoverContent
    Progress
    Table
    TabContent
    TabPane
    Tooltip
    UncontrolledTooltip])

#?(:clj
    (defn ^:private gen-bootstrap-inline-fn [tag]
      `(defmacro ~(symbol (kebab-case (str tag)))
         [opts# & children#]
         (let [ctor# '(.createFactory js/React (~(symbol (str ".-" (name tag))) js/Reactstrap))]
           (if (om-tools.dom/literal? opts#)
             (let [[opts# children#] (om-tools.dom/element-args opts# children#)]
               (cond
                 (every? (complement om-tools.dom/possible-coll?) children#)
                 `(~ctor# ~opts# ~@children#)

                 (and (= (count children#) 1) (vector? (first children#)))
                 `(~ctor# ~opts# ~@(-> children# first flatten))

                 :else
                 `(apply ~ctor# ~opts# (flatten (vector ~@children#)))))
             `(om-tools.dom/element ~ctor# ~opts# (vector ~@children#)))))))

#?(:clj
    (defmacro ^:private gen-bootstrap-inline-fns []
      `(do ~@(clojure.core/map gen-bootstrap-inline-fn bootstrap-tags))))

#?(:clj
    (gen-bootstrap-inline-fns))
