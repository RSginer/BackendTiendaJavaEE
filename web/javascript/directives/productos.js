(function () {
    angular.module("directivas-productos", [])
            .directive("listadoProductos", function () {
                return{
                    restrict: 'E',
                    templateUrl: './templates/includes/productos/lista-productos.html',
                    controller: function () {
                        this.repetirPuntuacionMedia = function (producto) {
                            this.repeticiones = [];
                            this.media = 0;
                            for (var i = 0; i < producto.reviews.length; i++) {
                                this.media = this.media + producto.reviews[i].estrellas;
                            }
                            this.media = Math.round(this.media / producto.reviews.length);
                            for (var i = 0; i < this.media; i++) {
                                this.repeticiones.push(i);
                            }
                            return this.repeticiones;
                        };
                    },
                    controllerAs: 'productos'
                };
            })
            .directive("tabsProducto", function () {
                return{
                    restrict: 'E',
                    templateUrl: 'templates/includes/productos/tabs-producto.html',
                    controller: function () {
                        this.tab = 1;
                        this.setTab = function (tab) {
                            this.tab = tab;
                        };
                        this.isSet = function (tab) {
                            return this.tab === tab;
                        };
                    },
                    controllerAs: 'tab'
                };
            })
            .directive("descripcionProducto", function () {
                return{
                    restrict: 'E',
                    templateUrl: 'templates/includes/productos/descripcion-producto.html'
                };
            })
            .directive("opinionesProducto", function () {
                return{
                    restrict: 'E',
                    templateUrl: 'templates/includes/productos/opiniones-producto.html',
                    scope: {
                        p: "=producto"
                    },
                    controller: function ($http) {
                        this.limite = 2;
                        this.aumentaLimite = function () {
                            this.limite += 3;
                        };
                        this.opinionEnviada = false;
                        this.setOpinionEnviada = function (o) {
                            this.opinionEnviada = o;
                        };
                        this.opinion = {
                            estrellas: 5,
                            idProducto: null,
                            idUsuario: 1,
                            autor: 'Anónimo',
                            imagen: "img/user.jpg",
                            fecha: ""
                        };
                        this.repetirEstrellas = function (s) {
                            repeticiones = [];
                            for (var i = 0; i < s; i++) {
                                repeticiones.push(i);
                            }
                            return repeticiones;
                        };
                        this.addOpinion = function (producto) {
                            this.opinion.fecha = Date.now();
                            this.opinion.idProducto = producto.id;
                            var req = {
                                method: 'POST',
                                url: 'api/reviews',
                                headers: {
                                    'Content-Type': undefined
                                },
                                data: this.opinion
                            };
                            $http(req).success(function () {
                                $http.get('api/reviews/' + producto.id).success(function(data){
                                    producto.reviews = data;
                                });
                                this.opinion = {
                                    estrellas: 5,
                                    idProducto: producto.id,
                                    idUsuario: 1,
                                    autor: 'Anónimo',
                                    imagen: "img/user.jpg",
                                    fecha: ""
                                }
                            });

                        };
                    },
                    controllerAs: 'opinionesCtrl'
                };
            });
})();
