module.exports = function (grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        names: {
            projectName: '<%= pkg.name %>',
            webrootTarget: 'target/classes/webroot',
            gruntTarget: 'target/grunt',
            gruntProjectTarget: '<%= names.gruntTarget %>/<%= names.projectName %>',
            webrootProjectTarget: '<%= names.webrootTarget %>/<%= names.projectName %>',
        },

        clean: ["<%= names.gruntTarget %>", '.tmp'],

        copy: {
            main: {
                expand: true,
                cwd: "<%= names.webrootTarget %>",
                src: ['**'],
                dest: '<%= names.gruntTarget %>'
            },
            final: {
                src: '<%= names.gruntProjectTarget %>/index.html',
                dest: '<%= names.webrootProjectTarget %>/index.html'
            }
        },

        useminPrepare: {
            html: '<%= names.gruntProjectTarget %>/dev/index.html'
        },

        rev: {
            files: {
                src: ['<%= names.gruntTarget %>/**/*.{js,css}']
            }
        },

        usemin: {
            html: '<%= names.gruntProjectTarget %>/dev/index.html'
        },

        uglify: {
            options: {
                ASCIIOnly: false // actually not necessary
            }
        },

        inline: {
            dist: {
                options:{
                    tag: '',
                    uglify: true
                },
                src: '<%= names.gruntProjectTarget %>/dev/index.html',
                dest: '<%= names.gruntProjectTarget %>/index.html'
            }
        },

        replace: {
          dist: {
            src: ['<%= names.gruntProjectTarget %>/index.html'],
            overwrite: true,
            replacements: [{
              from: '\'";</script>',
              to: '\'";\\x3c/script>'
            }]
          }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    //grunt.loadNpmTasks('grunt-contrib-concat');
    //grunt.loadNpmTasks('grunt-contrib-cssmin');
    //grunt.loadNpmTasks('grunt-contrib-uglify');
    //grunt.loadNpmTasks('grunt-rev');
    //grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-inline');
    grunt.loadNpmTasks('grunt-text-replace');

    // Tell Grunt what to do when we type "grunt" into the terminal
    grunt.registerTask('default', [
        //'clean', 'copy', 'useminPrepare', 'concat:generated', 'uglify:generated', 'inline'
        'clean', 'copy', 'inline', 'replace', 'copy:final'
        //'copy', 'useminPrepare', 'concat'/*, 'uglify', 'cssmin', 'rev'*/, 'usemin'
    ]);
};